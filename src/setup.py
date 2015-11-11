import os
from setuptools import setup

# Utility function to read the README file.
# Used for the long_description.  It's nice, because now 1) we have a top level
# README file and 2) it's easier to type in the README file than to put a raw
# string in below ...
def read(fname):
    return open(os.path.join(os.path.dirname(__file__), fname)).read()

setup(
    name = "courier_pygment",
    version = "0.0.1",
    author = "Joe Betz",
    author_email = "jpbetz@gmail.com",
    description = (""),
    license = "Apache License 2.0",
    keywords = "",
    url = "https://github.com/coursera/courier",
    packages=['courier_pygment'],
    classifiers=[
        "Development Status :: 3 - Alpha"
    ],
    entry_points='''[pygments.lexers]
courier = courier_pygment:CourierLexer
'''
)
